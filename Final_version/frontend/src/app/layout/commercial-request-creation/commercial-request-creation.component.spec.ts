import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommercialRequestCreationComponent } from './commercial-request-creation.component';

describe('CommercialRequestCreationComponent', () => {
  let component: CommercialRequestCreationComponent;
  let fixture: ComponentFixture<CommercialRequestCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommercialRequestCreationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommercialRequestCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
