import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockReasonDialogComponent } from './block-reason-dialog.component';

describe('BlockReasonDialogComponent', () => {
  let component: BlockReasonDialogComponent;
  let fixture: ComponentFixture<BlockReasonDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlockReasonDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BlockReasonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
