
Add a `CONTRIBUTING.md` with the same steps for running lint, tests, and commit‑hook expectations.

---

## 🎯 QUICK RECAP – ONE‑PAGE CHEAT SHEET  

| Step | Command (run from project root) | Outcome |
|------|----------------------------------|---------|
| **1** | `node -v && npm -v` | Verify Node |
| **2** | `ng new my-app --routing --style=scss --strict --skip-git` | Scaffold |
| **3** | `git init && git add . && git commit -m "init"` | Initialise repo |
| **4** | `ng generate module features/dashboard --routing --module app` | First lazy feature |
| **5** | `ng add @angular-eslint/schematics`<br>`npm i -D prettier eslint-config-prettier eslint-plugin-prettier`<br>`npx husky install`<br>`npx husky add .husky/pre-commit "npm run lint-staged"` | Lint + format + pre‑commit |
| **6** | `mkdir -p src/app/core/api && code src/app/core/api/api.service.ts` | Central API service |
| **7** | `ng add @ngrx/store@16` (or create RxJS state service) | State management |
| **8** | `ng add @briebug/jest-schematic`<br>`npm i -D @testing-library/angular @testing-library/jest-dom`<br>`ng e2e` | Unit + component + e2e testing |
| **9** | Add `.github/workflows/ci.yml` + `Dockerfile` | CI/CD pipeline |
| **10**| `npx sb init --type angular`<br>`npx compodoc -p tsconfig.json -s` | Storybook + docs |

---

## 📚 Where to Find the Official Angular References  

| Topic | Official link (Angular.io) |
|-------|----------------------------|
| **Getting started** | https://angular.io/guide/setup-local |
| **Workspace & CLI** | https://angular.io/cli |
| **Routing & lazy loading** | https://angular.io/guide/router |
| **Strict mode** | https://angular.io/guide/strict-mode |
| **Dependency injection (core/services)** | https://angular.io/guide/dependency-injection |
| **HttpClient & interceptors** | https://angular.io/guide/http |
| **Testing Overview** | https://angular.io/guide/testing |
| **Angular DevTools** | https://angular.io/guide/devtools |
| **Performance & bundle analysis** | https://angular.io/guide/build |
| **Internationalisation (i18n)** | https://angular.io/guide/i18n |
| **Deploying (Angular Universal, PWA, Docker)** | https://angular.io/guide/deployment |

Feel free to click any of those links for the deep‑dive that the Angular team maintains.

---

## 🚀  NEXT ACTIONS FOR YOU  

1. **Run steps 1‑3** on your machine.  
2. **Commit** after each major step – you’ll have a clean history to roll back if needed.  
3. **Open VS Code** and explore the folder structure; add the `core`, `shared`, `features` folders.  
4. **Implement steps 4‑6** (Dashboard feature + API service). Test it locally (`ng serve`).  
5. **Pick a state‑management approach** (NgRx or RxJS) and integrate it.  
6. **Add lint, Prettier & Husky** – then try committing a broken file to see the hook stop you.  
7. **Write a couple of unit tests** with Jest, then a simple Cypress flow.  
8. **Push the repo to GitHub** and enable the CI workflow; watch the pipeline run.  

If you hit any roadblock, let me know which step is giving trouble and I’ll walk you through the exact error messages and fixes. Happy coding! 🎉