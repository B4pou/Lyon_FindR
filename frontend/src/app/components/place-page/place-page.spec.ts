import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlacePage } from './place-page';

describe('PlacePage', () => {
  let component: PlacePage;
  let fixture: ComponentFixture<PlacePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlacePage],
    }).compileComponents();

    fixture = TestBed.createComponent(PlacePage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
