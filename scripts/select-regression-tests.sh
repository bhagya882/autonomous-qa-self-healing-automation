#!/usr/bin/env bash
set -euo pipefail

echo "Changed Java files in last commit:"
git diff --name-only HEAD~1 HEAD || true

echo
 echo "Suggested regression tests by changed module:"
for f in $(git diff --name-only HEAD~1 HEAD 2>/dev/null | grep -E '\\.(java|html|js|css)$' || true); do
  case "$f" in
    *LoginPage.java*) echo "TC_LOGIN_001 TC_LOGIN_002" ;;
    *CartPage.java*) echo "TC_CART_001" ;;
    *CheckoutPage.java*) echo "TC_CHECKOUT_001" ;;
    *HomePage.java*) echo "TC_HOME_001" ;;
  esac
done | sort -u
