// src/app/core/apis/auth/api.module.ts
import { NgModule, ModuleWithProviders, Optional, SkipSelf } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [HttpClientModule],
  providers: [],
})
export class AuthApiModule {
  constructor(@Optional() @SkipSelf() parentModule: AuthApiModule) {
    if (parentModule) {
      throw new Error('AuthApiModule is already loaded. Import only in AppModule.');
    }
  }

  static forRoot(): ModuleWithProviders<AuthApiModule> {
    return {
      ngModule: AuthApiModule,
      providers: [],
    };
  }
}
