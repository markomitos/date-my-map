import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapDaterComponent } from './map-dater-component';

describe('MapDaterComponent', () => {
  let component: MapDaterComponent;
  let fixture: ComponentFixture<MapDaterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapDaterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapDaterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
