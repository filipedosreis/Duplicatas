import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Log } from '../model/Log';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-arquivo-historico',
  standalone: true,
  imports: [CommonModule, RouterLink, MatButtonModule, MatTableModule, MatIconModule],
  templateUrl: './arquivo-historico.component.html',
  styleUrls: ['./arquivo-historico.component.css']
})
export class ArquivoHistoricoComponent {
  title = 'Logs dos Arquivos';
  loading = true;
  logs: Log[] = [];
  displayedColumns = ['id','criadoEm', 'arquivoId', 'acao','mensagem'];
  feedback: any = {};

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.loading = true;
    this.http.get<Log[]>('api/log/logs').subscribe((data: Log[]) => {
      this.logs = data;
      this.loading = false;
      this.feedback = {};
    });
  }

  protected readonly event = event;
}
