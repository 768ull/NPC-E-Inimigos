#!/usr/bin/env python3
"""Prepare the user's original Swordigo APK/APKM for mod development.

The commercial APK and extracted assets are kept locally and must not be
committed to this public repository.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import shutil
import sys
import zipfile
from collections import Counter
from pathlib import Path

REQUIRED = (
    "AndroidManifest.xml",
    "assets/resources/monsters.scl",
    "assets/resources/npc.scl",
    "assets/resources/npc_new.scl",
    "assets/resources/hiro.scl",
    "assets/resources/weapons.scl",
    "assets/resources/hero.scene",
)


def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def get_base_apk(source: Path, work: Path) -> Path:
    if source.suffix.lower() == ".apk":
        return source
    if source.suffix.lower() != ".apkm":
        raise ValueError("A origem deve ser um arquivo .apk ou .apkm")
    work.mkdir(parents=True, exist_ok=True)
    target = work / "input-base.apk"
    with zipfile.ZipFile(source) as bundle:
        bad = bundle.testzip()
        if bad:
            raise ValueError(f"APKM corrompido: erro CRC na entrada {bad}")
        if "base.apk" not in bundle.namelist():
            raise ValueError("O APKM não contém base.apk")
        with bundle.open("base.apk") as src, target.open("wb") as dst:
            shutil.copyfileobj(src, dst)
    return target


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", required=True, type=Path, help="Swordigo.apk original ou APKM íntegro")
    parser.add_argument("--work-dir", type=Path, default=Path("build/original"))
    args = parser.parse_args()

    source = args.input.expanduser().resolve()
    work = args.work_dir.expanduser().resolve()
    if not source.is_file():
        parser.error(f"Arquivo de origem não encontrado: {source}")

    work.mkdir(parents=True, exist_ok=True)
    try:
        base_apk = get_base_apk(source, work)
        assets_out = work / "assets" / "resources"
        assets_out.mkdir(parents=True, exist_ok=True)

        with zipfile.ZipFile(base_apk) as apk:
            bad = apk.testzip()
            if bad:
                raise ValueError(f"APK corrompido: erro CRC na entrada {bad}")
            names = set(apk.namelist())
            missing = [name for name in REQUIRED if name not in names]
            if missing:
                raise ValueError("Não parece ser a base esperada de Swordigo; faltam: " + ", ".join(missing))

            resource_entries = sorted(
                n for n in names if n.startswith("assets/resources/") and not n.endswith("/")
            )
            ext_counts: Counter[str] = Counter()
            hashes: dict[str, str] = {}

            for name in resource_entries:
                relative = Path(name).relative_to("assets/resources")
                target = assets_out / relative
                target.parent.mkdir(parents=True, exist_ok=True)
                with apk.open(name) as src, target.open("wb") as dst:
                    shutil.copyfileobj(src, dst)
                ext_counts[target.suffix.lower() or "(sem extensão)"] += 1
                if target.suffix.lower() in {".scl", ".scene", ".gstate"}:
                    hashes[name] = sha256_file(target)

            dex_files = sorted(n for n in names if n.startswith("classes") and n.endswith(".dex"))
            inventory = {
                "source_file": source.name,
                "source_size_bytes": source.stat().st_size,
                "source_sha256": sha256_file(source),
                "base_apk_sha256": sha256_file(base_apk),
                "validated_required_entries": list(REQUIRED),
                "dex_files": dex_files,
                "resource_file_count": len(resource_entries),
                "resource_extensions": dict(sorted(ext_counts.items())),
                "scl_scene_gstate_sha256": hashes,
                "extracted_to": str(assets_out),
                "note": "Recursos originais mantidos localmente; não enviar APK/binários comerciais ao repositório público.",
            }
            report = work / "original_game_inventory.json"
            report.write_text(json.dumps(inventory, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

    except (OSError, ValueError, zipfile.BadZipFile, zipfile.LargeZipFile) as exc:
        print(f"ERRO: {exc}", file=sys.stderr)
        return 2

    print(f"Origem validada: {source.name}")
    print(f"Recursos extraídos: {len(resource_entries)}")
    print(f"DEX encontrados: {len(dex_files)}")
    print(f"Inventário: {report}")
    print(f"Recursos: {assets_out}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
