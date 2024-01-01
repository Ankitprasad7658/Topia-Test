import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TopiaUserDetailComponent } from './topia-user-detail.component';

describe('TopiaUser Management Detail Component', () => {
  let comp: TopiaUserDetailComponent;
  let fixture: ComponentFixture<TopiaUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TopiaUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ topiaUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TopiaUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TopiaUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load topiaUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.topiaUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
