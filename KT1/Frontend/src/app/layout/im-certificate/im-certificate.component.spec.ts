import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImCertificateComponent } from './im-certificate.component';

describe('ImCertificateComponent', () => {
  let component: ImCertificateComponent;
  let fixture: ComponentFixture<ImCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImCertificateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ImCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
