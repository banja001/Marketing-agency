import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommercialCreationComponent } from './commercial-creation.component';

describe('CommercialCreationComponent', () => {
  let component: CommercialCreationComponent;
  let fixture: ComponentFixture<CommercialCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommercialCreationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommercialCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
