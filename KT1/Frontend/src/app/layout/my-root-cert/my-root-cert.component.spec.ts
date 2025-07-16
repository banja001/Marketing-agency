import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyRootCertComponent } from './my-root-cert.component';

describe('MyRootCertComponent', () => {
  let component: MyRootCertComponent;
  let fixture: ComponentFixture<MyRootCertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyRootCertComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyRootCertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
