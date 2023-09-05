import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { Arquivo } from '../model/arquivo';
import { ArquivoUploadService } from '../services/arquivo-upload.service';

@Component({
  selector: 'app-arquivo-upload',
  standalone: true,
  imports: [CommonModule, RouterLink, MatButtonModule, MatTableModule, MatIconModule],
  templateUrl: './arquivo-upload.component.html',
  styleUrls: ['./arquivo-upload.component.css']
})
export class ArquivoUploadComponent implements OnInit {
   title = 'Upload de Arquivo';
   file!: File;
   arquivo!: Arquivo;
   enviado = true;
   displayedColumns = ['id','nome', 'valido', 'enviado', 'acaoValidar', 'acaoEnviar'];
   feedback: any = {};
   arquivos: Array<Arquivo> = [];
   disableValidar = false;
   disableEnviar = true;

   constructor(private arquivoUploadService: ArquivoUploadService, private http: HttpClient) { }

   ngOnInit(): void {
   }

   selectFile(event: any) {
     this.file = event.target.files.item(0);
   }

   uploadFile() {
     this.arquivoUploadService.upload(this.file).subscribe({
       next: (data) => {
         this.arquivo = data;
         this.enviado = false;
         this.disableValidar = false;
         this.disableEnviar = true;
         this.arquivos = [];
         this.arquivos.push(this.arquivo);
         this.feedback = {type: 'success', message: 'Arquivo enviado com sucesso!'};
       },
       error: (e) => {
         this.feedback = {type: 'error', message: 'Erro ao fazer o upload do arquivo.'};
         console.log(e);
       }
     });
   }

  validar(arquivo: Arquivo): void {
      this.http.post(`api/arquivo/validar/${arquivo.id}`, new FormData()).subscribe({
        next: () => {
          this.feedback = {type: 'success', message: 'Arquivo CNAB validado com sucesso'};
          this.disableEnviar = false;
          this.disableValidar = true;
          this.arquivo.valido = true;
          setTimeout(() => {
            this.ngOnInit();
          }, 1000);
        },
        error: (err: HttpErrorResponse) => {
          let arr = err.error.errors;
          let s = err.error.message + '\n';
          for (let data of arr) {
              s += JSON.stringify(data) + '\n';
          }
          this.feedback = {type: 'error', message: s};

        }
      });
  }

  enviar(arquivo: Arquivo): void {
      this.http.post(`api/arquivo/enviar/${arquivo.id}`, new FormData()).subscribe({
        next: (resp: any) => {
          let arr = resp.data.transactions;
          let s = resp.message + '\n';
          for (let data of arr) {
              s += JSON.stringify(data) + '\n';
          }
          this.feedback = {type: 'success', message: s};

          this.disableEnviar = true;
          this.disableEnviar = true;
          this.arquivo.enviado = true;
          setTimeout(() => {
            this.ngOnInit();
          }, 1000);
        },
        error: (err: HttpErrorResponse) => {
          let arr = err.error.errors;
          let s = err.error.message + '\n';
          for (let data of arr) {
              s += JSON.stringify(data) + '\n';
          }
          this.feedback = {type: 'error', message: s};

        }
      });
  }

 }
