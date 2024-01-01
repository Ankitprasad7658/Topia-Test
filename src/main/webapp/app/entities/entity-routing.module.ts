import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'topia-user',
        data: { pageTitle: 'TopiaUsers' },
        loadChildren: () => import('./topia-user/topia-user.module').then(m => m.TopiaUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
