import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordlessVerificationComponent } from './passwordless-verification.component';

describe('PasswordlessVerificationComponent', () => {
  let component: PasswordlessVerificationComponent;
  let fixture: ComponentFixture<PasswordlessVerificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PasswordlessVerificationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PasswordlessVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
