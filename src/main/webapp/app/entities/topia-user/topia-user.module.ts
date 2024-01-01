import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TopiaUserComponent } from './list/topia-user.component';
import { TopiaUserDetailComponent } from './detail/topia-user-detail.component';
import { TopiaUserUpdateComponent } from './update/topia-user-update.component';
import { TopiaUserDeleteDialogComponent } from './delete/topia-user-delete-dialog.component';
import { TopiaUserRoutingModule } from './route/topia-user-routing.module';

@NgModule({
  imports: [SharedModule, TopiaUserRoutingModule],
  declarations: [TopiaUserComponent, TopiaUserDetailComponent, TopiaUserUpdateComponent, TopiaUserDeleteDialogComponent],
  entryComponents: [TopiaUserDeleteDialogComponent],
})
export class TopiaUserModule {}
