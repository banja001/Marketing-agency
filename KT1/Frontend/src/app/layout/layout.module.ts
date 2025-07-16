import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { CertificateViewComponent } from './certificate-view/certificate-view.component';
import { MyRootCertComponent } from './my-root-cert/my-root-cert.component';
import { MaterialModule } from '../infrastructure/material/material.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ImCertificateComponent } from './im-certificate/im-certificate.component';
import { MatTableModule } from '@angular/material/table';
import { MatTooltip } from '@angular/material/tooltip';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    NavbarComponent,
    CertificateViewComponent,
    MyRootCertComponent,
    ImCertificateComponent,
    CreateCertificateComponent,
    SearchComponent,
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    MatTableModule,
    MatTooltip,
  ],
  exports: [
    NavbarComponent,
    CertificateViewComponent,
    MyRootCertComponent,
    ImCertificateComponent,
    CreateCertificateComponent,
    SearchComponent,
  ],
})
export class LayoutModule {}
