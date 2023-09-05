import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArquivoListaComponent } from './arquivo-lista.component';

describe('ArquivoListaComponent', () => {
  let component: ArquivoListaComponent;
  let fixture: ComponentFixture<ArquivoListaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ArquivoListaComponent]
    });
    fixture = TestBed.createComponent(ArquivoListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
