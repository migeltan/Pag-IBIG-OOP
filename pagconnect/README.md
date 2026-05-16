# Pag-CONNECT

Pag-CONNECT is a web-based Pag-IBIG membership management system built using Laravel and MySQL. The project is designed to manage member records, contributions, membership information, and related services through a centralized web application.

## Tech Stack

* Backend: PHP / Laravel
* Database: MySQL
* Frontend: Blade Templates / HTML / CSS / JavaScript
* Version Control: Git + GitHub

---

# Project Setup Guide

## 1. Clone the Repository

```bash
git clone <your-repository-url>
cd pagconnect
```

---

## 2. Install PHP Dependencies

Make sure Composer is installed.

```bash
composer install
```

This will generate the `vendor/` folder required by Laravel.

---

## 3. Create the Environment File

Copy the example environment file:

### Windows PowerShell

```powershell
copy .env.example .env
```

### macOS / Linux

```bash
cp .env.example .env
```

---

## 4. Configure Database Connection

Open the `.env` file and edit the database section:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=pagibig
DB_USERNAME=root
DB_PASSWORD=
```

Make sure the database already exists in MySQL.

---

## 5. Import the Database

The SQL dump is located at:

```txt
database/sql/pagibig.sql
```

### Using MySQL Workbench

1. Open MySQL Workbench
2. Create a schema named:

```txt
pagibig
```

3. Go to:

```txt
Server → Data Import
```

4. Choose:

```txt
Import from Self-Contained File
```

5. Select:

```txt
database/sql/pagibig.sql
```

6. Select target schema:

```txt
pagibig
```

7. Click:

```txt
Start Import
```

---

## 6. Generate Laravel Application Key

```bash
php artisan key:generate
```

---

## 7. Run the Application

```bash
php artisan serve
```

Open the generated local URL in your browser.

Example:

```txt
http://127.0.0.1:8000
```

---

# Repository Setup Using Forking

## 1. Fork the Repository

Each team member should first fork the repository on GitHub.

1. Open the main repository on GitHub.
2. Click the `Fork` button on the top-right corner.
3. This creates your own copy of the repository under your GitHub account.

---

## 2. Clone Your Fork Using VS Code

### Option A — Using VS Code GUI

1. Open Visual Studio Code
2. Press:

```txt
Ctrl + Shift + P
```

3. Search:

```txt
Git: Clone
```

4. Paste your forked repository URL.
5. Choose a local folder.
6. Open the project after cloning.

---

### Option B — Using VS Code Terminal

Open a terminal inside VS Code and run:

```bash
git clone <your-fork-url>
cd pagconnect
```

---

## 3. Create Your Own Development Branch

After cloning, DO NOT work directly on `main`.

Inside the VS Code terminal:

```bash
git checkout -b your-branch-name
```

Example:

```bash
git checkout -b migel-feature-ui
```

This creates and switches to your personal development branch.

---

## 4. Push Your Branch to GitHub

```bash
git push -u origin your-branch-name
```

Example:

```bash
git push -u origin migel-feature-ui
```

---

## 5. Sync With Latest Changes

Before starting development each day:

```bash
git pull origin main
```

If working on your branch:

```bash
git checkout your-branch-name
```

Then merge the latest updates:

```bash
git merge main
```

---

# Git Workflow

## Pull Latest Changes

```bash
git pull
```

## Add Changes

```bash
git add .
```

## Commit Changes

```bash
git commit -m "Your message here"
```

## Push Changes

```bash
git push
```

---

# Important Notes

## Do NOT Upload These Files/Folders

The following should remain excluded from GitHub:

```txt
/vendor
/node_modules
.env
```

---

## Upload These Files

Important project files that SHOULD be uploaded:

```txt
app/
routes/
resources/
database/sql/pagibig.sql
.env.example
composer.json
```

---

# Recommended Development Practices

* Do not manually edit the production database.
* Keep database updates synchronized with the team.
* Use Git commits with clear messages.
* Test features locally before pushing.
* Pull latest changes before starting development.

---

# License

This project is for academic and educational purposes only.
