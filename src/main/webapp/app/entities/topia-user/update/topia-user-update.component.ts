import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITopiaUser, TopiaUser } from '../topia-user.model';
import { TopiaUserService } from '../service/topia-user.service';

@Component({
  selector: 'jhi-topia-user-update',
  templateUrl: './topia-user-update.component.html',
})
export class TopiaUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    firstName: [],
    lastName: [],
    dateOfBirth: [],
    userName: [],
    email: [],
    createdDate: [],
    createdBy: [],
    updatedDate: [],
    updatedBy: [],
    status: [],
  });

  constructor(protected topiaUserService: TopiaUserService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ topiaUser }) => {
      this.updateForm(topiaUser);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const topiaUser = this.createFromForm();
    if (topiaUser.id !== undefined) {
      this.subscribeToSaveResponse(this.topiaUserService.update(topiaUser));
    } else {
      this.subscribeToSaveResponse(this.topiaUserService.create(topiaUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITopiaUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(topiaUser: ITopiaUser): void {
    this.editForm.patchValue({
      id: topiaUser.id,
      name: topiaUser.name,
      dateOfBirth: topiaUser.dateOfBirth,
      userName: topiaUser.userName,
      email: topiaUser.email,
      createdDate: topiaUser.createdDate,
      createdBy: topiaUser.createdBy,
      updatedDate: topiaUser.updatedDate,
      updatedBy: topiaUser.updatedBy,
      status: topiaUser.status,
    });
  }

  protected createFromForm(): ITopiaUser {
    return {
      ...new TopiaUser(),
      id: this.editForm.get(['id'])!.value,
      firstName : this.editForm.get(['firstName'])!.value,
      lastName : this.editForm.get(['lastName'])!.value,
      name: this.editForm.get(['name'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      email: this.editForm.get(['email'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      updatedDate: this.editForm.get(['updatedDate'])!.value,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
