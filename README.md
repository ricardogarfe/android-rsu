<a href="https://scan.coverity.com/projects/ricardogarfe-android-rsu">
  <img alt="Coverity Scan Build Status"
       src="https://img.shields.io/coverity/scan/7751.svg"/>
</a>
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](LICENSE)
[![Build Status](https://travis-ci.org/ricardogarfe/android-rsu.svg?branch=master)](https://travis-ci.org/ricardogarfe/android-rsu)
[![Circle CI](https://circleci.com/gh/ricardogarfe/android-rsu.svg?style=shield)](https://circleci.com/gh/ricardogarfe/android-rsu)

# android-rsu

Access to [Ayuntamiento de Valencia Open Data API](http://www.ayto-valencia.es/ayuntamiento/DatosAbiertos.nsf/0/2113BD9D1693D7EAC1257C6600449981/$FILE/API%20APPCIUDAD%20v3.pdf?OpenElement&lang=1) for type of waste.

## Request

URL: http://mapas.valencia.es/lanzadera/gps/contenedores/{tipo}/{lat}{lon}

## Parameters

Depending on the type of waste they want to see. Here is a table of the available types:

| Batteries | Oil    | Clothes | Waste    | Bottling | Cardboard | Glass  |
|-----------|--------|---------|----------|----------|-----------|--------|
| pilas     | aceite | ropa    | residuos | envases  | carton    | vidrio |

## Response

Basic response:

```json
[
  {
    "latDestino": 39472993,
    "lonDestino": -385258,
    "distancia": 275,
    "titulo": "CONTENEDORES DE PILAS",
    "mensaje": "MERCADO ROJAS CLEMENTE\nBotanico\nNúmero de contenedores: 1"
  },
  {
    "latDestino": 39473757,
    "lonDestino": -378457,
    "distancia": 407,
    "titulo": "CONTENEDORES DE PILAS",
    "mensaje": "MERCADO CENTRAL\nPlaza Mercado\nNúmero de contenedores: 1"
  },
  {
    "latDestino": 39472640,
    "lonDestino": -387118,
    "distancia": 411,
    "titulo": "CONTENEDORES DE PILAS",
    "mensaje": "COLEGIO JESUS-MARIA\nG.V. Fernando el Catolico, 37\nNúmero de contenedores: 1"
  }
]
 ```
