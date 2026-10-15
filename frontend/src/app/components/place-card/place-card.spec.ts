import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlaceCard } from './place-card';

describe('PlaceCard', () => {
  let component: PlaceCard;
  let fixture: ComponentFixture<PlaceCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaceCard],
    }).compileComponents();

    fixture = TestBed.createComponent(PlaceCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
