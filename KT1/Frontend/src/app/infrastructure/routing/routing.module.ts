import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../auth/login/login.component';
import { CertificateViewComponent } from '../../layout/certificate-view/certificate-view.component';
import { MyRootCertComponent } from '../../layout/my-root-cert/my-root-cert.component';
import { ImCertificateComponent } from '../../layout/im-certificate/im-certificate.component';
import { CreateCertificateComponent } from '../../layout/create-certificate/create-certificate.component';
import { SearchComponent } from '../../layout/search/search.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'certView', component: CertificateViewComponent },
  { path: 'myRoot', component: MyRootCertComponent },
  { path: 'myImCert', component: ImCertificateComponent },
  { path: 'createCert/:username', component: CreateCertificateComponent },
  { path: 'search', component: SearchComponent },
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class RoutingModule {}
