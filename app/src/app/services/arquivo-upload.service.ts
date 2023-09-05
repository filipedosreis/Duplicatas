import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Arquivo } from '../model/arquivo';

@Injectable({
  providedIn: 'root'
})
export class ArquivoUploadService {

  private baseUrl = "http://localhost:8080"
  constructor(private httpClient: HttpClient) { }

  upload(arquivo: File): Observable<Arquivo> {
    const formData: FormData = new FormData();
    formData.append('file', arquivo);
    return this.httpClient.post<Arquivo>(`${this.baseUrl}/api/arquivo`, formData);
  }
}
