import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TopiaUserService } from '../service/topia-user.service';
import { ITopiaUser, TopiaUser } from '../topia-user.model';

import { TopiaUserUpdateComponent } from './topia-user-update.component';

describe('TopiaUser Management Update Component', () => {
  let comp: TopiaUserUpdateComponent;
  let fixture: ComponentFixture<TopiaUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let topiaUserService: TopiaUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TopiaUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TopiaUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TopiaUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    topiaUserService = TestBed.inject(TopiaUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const topiaUser: ITopiaUser = { id: 456 };

      activatedRoute.data = of({ topiaUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(topiaUser));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TopiaUser>>();
      const topiaUser = { id: 123 };
      jest.spyOn(topiaUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ topiaUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: topiaUser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(topiaUserService.update).toHaveBeenCalledWith(topiaUser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TopiaUser>>();
      const topiaUser = new TopiaUser();
      jest.spyOn(topiaUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ topiaUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: topiaUser }));
      saveSubject.complete();

      // THEN
      expect(topiaUserService.create).toHaveBeenCalledWith(topiaUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TopiaUser>>();
      const topiaUser = { id: 123 };
      jest.spyOn(topiaUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ topiaUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(topiaUserService.update).toHaveBeenCalledWith(topiaUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
