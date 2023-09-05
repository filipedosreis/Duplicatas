import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Arquivo } from '../model/arquivo';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-arquivo-lista',
  standalone: true,
  imports: [CommonModule, RouterLink, MatButtonModule, MatTableModule, MatIconModule],
  templateUrl: './arquivo-lista.component.html',
  styleUrls: ['./arquivo-lista.component.css']
})
export class ArquivoListaComponent {
  title = 'Arquivos';
  loading = true;
  arquivos: Arquivo[] = [];
  displayedColumns = ['id','name','criadoEm','valido','enviado'];
  feedback: any = {};

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.loading = true;
    this.http.get<Arquivo[]>('api/arquivo/arquivos').subscribe((data: Arquivo[]) => {
      this.arquivos = data;
      this.loading = false;
      this.feedback = {};
    });
  }

  delete(arquivo: Arquivo): void {
    if (confirm(`Are you sure you want to delete ${arquivo.nome}?`)) {
      this.http.delete(`api/group/${arquivo.id}`).subscribe({
        next: () => {
          this.feedback = {type: 'success', message: 'Delete was successful!'};
          setTimeout(() => {
            this.ngOnInit();
          }, 1000);
        },
        error: () => {
          this.feedback = {type: 'warning', message: 'Error deleting.'};
        }
      });
    }
  }

  protected readonly event = event;
}
