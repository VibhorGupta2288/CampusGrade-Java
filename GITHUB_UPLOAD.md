# GitHub Upload Steps

The VITyarthi portal requires the repository to be Public and the submitted URL to be the repository root, not a `/tree/...` or `/blob/...` URL.

From the extracted project folder:

```bash
git init
git add .
git commit -m "Initial CampusGrade project"
```

Create a new empty repository on GitHub, then connect it using the URL GitHub gives you:

```bash
git branch -M main
git remote add origin https://github.com/<github-username>/<repo-name>.git
git push -u origin main
```

Before submission, verify that the public repository opens in a browser and that `README.md`, `statement.md`, `src/`, `tests/`, `data/`, and `docs/` are visible at the root.

Submit this exact style of root URL on VITyarthi:

```text
https://github.com/<github-username>/<repo-name>
```

Do not submit a URL containing `/tree/main/`, `/blob/main/`, or another subpath.
