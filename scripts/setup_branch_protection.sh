#!/usr/bin/env bash
# Requer gh CLI autenticado: https://cli.github.com/

set -euo pipefail

REPO="${1:-<owner>/<repo>}"
BRANCH="${2:-main}"

echo "Configurando proteção da branch $BRANCH em $REPO..."

# Require PR + reviews + status checks + signed commits
gh api   -X PUT   -H "Accept: application/vnd.github+json"   "/repos/${REPO}/branches/${BRANCH}/protection"   -f required_status_checks.strict=true   -f enforce_admins=true   -f required_pull_request_reviews.required_approving_review_count=1   -f required_pull_request_reviews.dismiss_stale_reviews=true   -f restrictions=   -F required_signatures=true

# Required checks (adicione o nome exato do job do CI)
# Exemplo para o job "build-test"
gh api   -X PATCH   -H "Accept: application/vnd.github+json"   "/repos/${REPO}/branches/${BRANCH}/protection/required_status_checks"   -f strict=true   -F contexts[]="CI - Build & Test (Spring Boot)" || true

# Merge Queue (habilitar em repo settings via API - beta endpoints podem mudar)
echo "Ative a Merge Queue via Settings no GitHub UI (Pull Requests → Merge queue)."
echo "Depois, configure a regra que exige checks e revisões para entrar na fila."
