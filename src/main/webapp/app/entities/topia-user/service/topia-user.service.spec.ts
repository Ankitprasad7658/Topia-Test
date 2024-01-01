import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITopiaUser, TopiaUser } from '../topia-user.model';

import { TopiaUserService } from './topia-user.service';

describe('TopiaUser Service', () => {
  let service: TopiaUserService;
  let httpMock: HttpTestingController;
  let elemDefault: ITopiaUser;
  let expectedResult: ITopiaUser | ITopiaUser[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TopiaUserService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      dateOfBirth: currentDate,
      userName: 'AAAAAAA',
      email: 'AAAAAAA',
      createdDate: currentDate,
      createdBy: 0,
      updatedDate: currentDate,
      updatedBy: 0,
      status: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateOfBirth: currentDate.format(DATE_FORMAT),
          createdDate: currentDate.format(DATE_FORMAT),
          updatedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TopiaUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateOfBirth: currentDate.format(DATE_FORMAT),
          createdDate: currentDate.format(DATE_FORMAT),
          updatedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new TopiaUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TopiaUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          dateOfBirth: currentDate.format(DATE_FORMAT),
          userName: 'BBBBBB',
          email: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          createdBy: 1,
          updatedDate: currentDate.format(DATE_FORMAT),
          updatedBy: 1,
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TopiaUser', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          dateOfBirth: currentDate.format(DATE_FORMAT),
          userName: 'BBBBBB',
          email: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          createdBy: 1,
          status: 'BBBBBB',
        },
        new TopiaUser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TopiaUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          dateOfBirth: currentDate.format(DATE_FORMAT),
          userName: 'BBBBBB',
          email: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          createdBy: 1,
          updatedDate: currentDate.format(DATE_FORMAT),
          updatedBy: 1,
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOfBirth: currentDate,
          createdDate: currentDate,
          updatedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TopiaUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTopiaUserToCollectionIfMissing', () => {
      it('should add a TopiaUser to an empty array', () => {
        const topiaUser: ITopiaUser = { id: 123 };
        expectedResult = service.addTopiaUserToCollectionIfMissing([], topiaUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(topiaUser);
      });

      it('should not add a TopiaUser to an array that contains it', () => {
        const topiaUser: ITopiaUser = { id: 123 };
        const topiaUserCollection: ITopiaUser[] = [
          {
            ...topiaUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addTopiaUserToCollectionIfMissing(topiaUserCollection, topiaUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TopiaUser to an array that doesn't contain it", () => {
        const topiaUser: ITopiaUser = { id: 123 };
        const topiaUserCollection: ITopiaUser[] = [{ id: 456 }];
        expectedResult = service.addTopiaUserToCollectionIfMissing(topiaUserCollection, topiaUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(topiaUser);
      });

      it('should add only unique TopiaUser to an array', () => {
        const topiaUserArray: ITopiaUser[] = [{ id: 123 }, { id: 456 }, { id: 48602 }];
        const topiaUserCollection: ITopiaUser[] = [{ id: 123 }];
        expectedResult = service.addTopiaUserToCollectionIfMissing(topiaUserCollection, ...topiaUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const topiaUser: ITopiaUser = { id: 123 };
        const topiaUser2: ITopiaUser = { id: 456 };
        expectedResult = service.addTopiaUserToCollectionIfMissing([], topiaUser, topiaUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(topiaUser);
        expect(expectedResult).toContain(topiaUser2);
      });

      it('should accept null and undefined values', () => {
        const topiaUser: ITopiaUser = { id: 123 };
        expectedResult = service.addTopiaUserToCollectionIfMissing([], null, topiaUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(topiaUser);
      });

      it('should return initial array if no TopiaUser is added', () => {
        const topiaUserCollection: ITopiaUser[] = [{ id: 123 }];
        expectedResult = service.addTopiaUserToCollectionIfMissing(topiaUserCollection, undefined, null);
        expect(expectedResult).toEqual(topiaUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
