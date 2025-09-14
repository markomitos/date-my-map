import { Component } from '@angular/core';
import { MapDaterComponent } from './features/map-dater/map-dater-component/map-dater-component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MapDaterComponent],
  template: `
    <main>
      <app-map-dater />
    </main>
  `,
})
export class AppComponent {}
