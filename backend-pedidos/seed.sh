#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/.env"

if [[ ! -f "$ENV_FILE" ]]; then
	echo "No se encontró .env en $SCRIPT_DIR" >&2
	exit 1
fi

set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

: "${DB_HOST:?Falta DB_HOST en .env}"
: "${DB_NAME:?Falta DB_NAME en .env}"
: "${DB_USER:?Falta DB_USER en .env}"
: "${DB_PASS:?Falta DB_PASS en .env}"

docker run --rm \
	-e PGPASSWORD="$DB_PASS" \
	postgres:16-alpine \
	psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" \
	-c "SELECT setval(pg_get_serial_sequence('pedidos', 'id'), coalesce(max(id),0) + 1, false) FROM pedidos;"

docker run --rm \
	-e PGPASSWORD="$DB_PASS" \
	postgres:16-alpine \
	psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" \
	-c "SELECT setval(pg_get_serial_sequence('detalle_pedidos', 'id'), coalesce(max(id),0) + 1, false) FROM detalle_pedidos;"
