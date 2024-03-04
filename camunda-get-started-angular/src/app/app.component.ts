import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
// import { CamundaRestService } from '@core/services/workflow/camunda-rest.service';
// import { BaseComponent } from '@core/helpers/base.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { switchMap, takeUntil } from 'rxjs/operators';
// @ts-ignore
import * as BpmnViewer from 'bpmn-js/dist/bpmn-viewer.production.min.js';
// import 'bpmn-js/dist/assets/diagram-js.css';
// import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
// @ts-ignore
import * as BpmnNavigatedViewer from 'bpmn-js/dist/bpmn-navigated-viewer.production.min.js';
// @ts-ignore
import * as camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json';
import { Observable } from 'rxjs/internal/Observable';
import { from } from 'rxjs/internal/observable/from';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'camunda-get-started-angular';

  constructor(
    public activeModal: NgbActiveModal
  ) // private camundaRestService: CamundaRestService
  {}

  ngOnInit(): void {
    
  }

  zoomIn() {
    console.log('zoom In');
  }
  zoomOut() {
    console.log('zoom Out');
  }
}
