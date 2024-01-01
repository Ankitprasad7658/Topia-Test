import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TopiaUserComponent } from '../list/topia-user.component';
import { TopiaUserDetailComponent } from '../detail/topia-user-detail.component';
import { TopiaUserUpdateComponent } from '../update/topia-user-update.component';
import { TopiaUserRoutingResolveService } from './topia-user-routing-resolve.service';

const topiaUserRoute: Routes = [
  {
    path: '',
    component: TopiaUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TopiaUserDetailComponent,
    resolve: {
      topiaUser: TopiaUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TopiaUserUpdateComponent,
    resolve: {
      topiaUser: TopiaUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TopiaUserUpdateComponent,
    resolve: {
      topiaUser: TopiaUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(topiaUserRoute)],
  exports: [RouterModule],
})
export class TopiaUserRoutingModule {}
