import { TestBed } from '@angular/core/testing';

import { PostClient } from './post-client';

describe('PostClient', () => {
  let service: PostClient;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
