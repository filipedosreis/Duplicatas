import { Arquivo } from './arquivo';

export class Log {
  id!: number;
  acao!: string;
  arquivo!: Arquivo;
  mensagem!: string;
  criadoEm!: Date;
}
