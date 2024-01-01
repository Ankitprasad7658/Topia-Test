import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITopiaUser } from '../topia-user.model';

@Component({
  selector: 'jhi-topia-user-detail',
  templateUrl: './topia-user-detail.component.html',
})
export class TopiaUserDetailComponent implements OnInit {
  topiaUser: ITopiaUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ topiaUser }) => {
      this.topiaUser = topiaUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
