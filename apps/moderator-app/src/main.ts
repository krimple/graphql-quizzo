import 'style-loader!:../../node_modules/primeicons/primeicons.css';
import 'style-loader!:../../node_modules/primeng/resources/themes/bootstrap/theme.css';
import 'style-loader!:../../node_modules/primeng/resources/primeng.css';


import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
