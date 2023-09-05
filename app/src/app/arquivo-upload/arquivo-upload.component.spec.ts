import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArquivoUploadComponent } from './arquivo-upload.component';

describe('ArquivoUploadComponent', () => {
  let component: ArquivoUploadComponent;
  let fixture: ComponentFixture<ArquivoUploadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ArquivoUploadComponent]
    });
    fixture = TestBed.createComponent(ArquivoUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
