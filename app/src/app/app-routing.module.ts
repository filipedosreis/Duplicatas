import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ArquivoListaComponent } from './arquivo-lista/arquivo-lista.component';
import { ArquivoUploadComponent } from './arquivo-upload/arquivo-upload.component';
import { ArquivoHistoricoComponent } from './arquivo-historico/arquivo-historico.component';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent  },
  { path: 'arquivos', component: ArquivoListaComponent  },
  { path: 'arquivo-upload', component: ArquivoUploadComponent },
  { path: '', redirectTo: 'arquivo-upload', pathMatch: 'full' },
  { path: 'arquivo-historico', component: ArquivoHistoricoComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
