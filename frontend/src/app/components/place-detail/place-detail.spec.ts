import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlaceDetail } from './place-detail';

describe('PlaceDetail', () => {
  let component: PlaceDetail;
  let fixture: ComponentFixture<PlaceDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaceDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(PlaceDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
