import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArquivoHistoricoComponent } from './arquivo-historico.component';

describe('ArquivoHistoricoComponent', () => {
  let component: ArquivoHistoricoComponent;
  let fixture: ComponentFixture<ArquivoHistoricoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ArquivoHistoricoComponent]
    });
    fixture = TestBed.createComponent(ArquivoHistoricoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
