import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {MapDaterComponent} from './features/map-dater/map-dater-component/map-dater-component';

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
export class App {
  protected readonly title = signal('date-my-map');
}
