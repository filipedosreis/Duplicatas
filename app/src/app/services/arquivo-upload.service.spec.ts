import { TestBed } from '@angular/core/testing';

import { ArquivoUploadService } from './arquivo-upload.service';

describe('ArquivoUploadService', () => {
  let service: ArquivoUploadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArquivoUploadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
