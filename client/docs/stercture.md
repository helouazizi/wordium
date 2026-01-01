
my-app/
│
├─ src/
│   ├─ app/
│   │   ├─ core/                 # Singleton services (Auth, Http, Logger, etc.)
│   │   ├─ shared/               # Re‑usable components, pipes, directives
│   │   ├─ features/
│   │   │   ├─ dashboard/
│   │   │   │   ├─ dashboard.module.ts
│   │   │   │   ├─ dashboard-routing.module.ts
│   │   │   │   ├─ components/
│   │   │   │   └─ pages/
│   │   │   └─ users/
│   │   ├─ app-routing.module.ts
│   │   └─ app.module.ts
│   ├─ assets/                   # Images, fonts, static files
│   ├─ environments/
│   │   ├─ environment.ts        # dev
│   │   └─ environment.prod.ts   # prod
│   ├─ styles.scss               # Global styles
│   └─ main.ts
│
├─ e2e/                          # Cypress / Protractor end‑to‑end tests
├─ .editorconfig
├─ .eslintrc.json
├─ .prettierrc
├─ angular.json
├─ tsconfig.json
└─ package.json


