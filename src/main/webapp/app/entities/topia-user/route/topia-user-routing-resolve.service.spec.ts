import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITopiaUser, TopiaUser } from '../topia-user.model';
import { TopiaUserService } from '../service/topia-user.service';

import { TopiaUserRoutingResolveService } from './topia-user-routing-resolve.service';

describe('TopiaUser routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TopiaUserRoutingResolveService;
  let service: TopiaUserService;
  let resultTopiaUser: ITopiaUser | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TopiaUserRoutingResolveService);
    service = TestBed.inject(TopiaUserService);
    resultTopiaUser = undefined;
  });

  describe('resolve', () => {
    it('should return ITopiaUser returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTopiaUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTopiaUser).toEqual({ id: 123 });
    });

    it('should return new ITopiaUser if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTopiaUser = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTopiaUser).toEqual(new TopiaUser());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TopiaUser })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTopiaUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTopiaUser).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
