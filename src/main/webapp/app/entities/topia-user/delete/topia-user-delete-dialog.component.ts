import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITopiaUser } from '../topia-user.model';
import { TopiaUserService } from '../service/topia-user.service';

@Component({
  templateUrl: './topia-user-delete-dialog.component.html',
})
export class TopiaUserDeleteDialogComponent {
  topiaUser?: ITopiaUser;

  constructor(protected topiaUserService: TopiaUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.topiaUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
