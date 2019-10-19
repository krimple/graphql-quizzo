import {NgModule} from '@angular/core';
import {AgGridModule} from 'ag-grid-angular';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {MessageService, TreeTableModule} from 'primeng/primeng';
import {ToastModule} from "primeng/toast";

@NgModule({
  imports: [
    NgbModule,
    TreeTableModule,
    ToastModule,
    AgGridModule.withComponents([])
  ],
  providers: [
    MessageService
  ],
  exports: [
    NgbModule,
    TreeTableModule,
    ToastModule,
    AgGridModule

  ]
})
export class ComponentIntegrationModule { }
