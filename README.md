# Clinics

O principal objetivo deste projeto é o desenvolvimento de uma aplicação empresarial clínica, para a gestão de dados/sinais biomédicos de pessoas com doença cardiovascular.

## How to set it up?

U should have docker running and in the root project directory

```
make up
```

To compose you amazing changes

```
make deploy
```

## You can have some issues

If something like this appear "Failed to start service jboss.deployment.unit."clinics.war" ... "

Run this following commands

```
make down
make up
make deploy
```

## Entities (Columns & Features)

### Persons

This is a superclass that involves Administrators, Doctors, Patients.

#### Columns
 - username **(UNIQUE PK)**
 - email **(UNIQUE)** :as username and can be null
 - password :should be encripted in client side
 - name
 - gender :could be [ ] - [M]ale [ ] - [F]emale [ ] - [O]ther
 - created_at
 - updated_at
 - deleted_at
#### Features
 - Login

### Administrators (extend Persons)

#### Column
 - doctors **(M to 1)**

#### Features
 - Adicionar Tipos de Prescrições
 - Visualização de histórico de dados/sinais biométricos dos utentes (em anónimo talvez, não têm de saber quem são os utentes)

### Patients (extend Persons)

As prescrições do utente vão ser baseadas nos valores dos dados biométricos, então a prescrição é aplicada a um tipo de dado biométrico, e esse tipo de dado biométrico se estiver irregular significa que o utente terá essa mesma prescrição.
fazendo assim uma coligação com as prescriptions de **(N to M)**

#### Column
 - healthNo **(UNIQUE)** :Número de Utente
 - doctors **(N to M)**
 - biometric_Data **(N to M)**
 - Created_by :only by Médicos
#### Features
 - Inscrição Própria
 - Consultas de estados próprios
 - Visualização de histórico de dados/sinais biométricos (Próprio)

### Doctors (extend Persons)

#### Columns
 - specialty **(String)**
 - patients **(N to M)**
 - created_by **(FK_USER)** :only by Administradores
 - prescriptions **(1 to M)**
#### Features
 - Inscrição de Utentes
 - Consultas de estados de Utentes
 - Atribuir Prescrições aos Utentes
 - Gerir os dados/sinais biométricos dos seus utentes

### Biometric_Data_Type (Temperatura corporal, Frequência cardíaca)

#### Columns
 - id **(UNIQUE PK)**
 - name :temperatura corporal (pode este campo ser a PK?)
 - min :30(º)
 - max :45(º)
 - measurement_unit **(STRING)** :ºC (Graus)
#### Features
 - Notificar os Utentes e Médicos

### Biometric_Data (Resultados)

#### Columns
 - id **(UNIQUE PK)**
 - Biometric_Data_Issue **(1 to M)**
 - values **(Integer)**
 - notes **(String)**

#### Features

### Biometric_Data_Issue

#### Columns

 - id **(UNIQUE PK)**
 - name **(UNIQUE)** :can this be unique?
 - min **(Int)**
 - max **(Int)**
 - Biometric_Data_Type **(M to 1)**

#### Features


### Prescriptions (uma prescrição de exercício físico para doentes com obesidade, Alimentação, Exercício regular)

#### Columns

 - id **(UNIQUE PK)**
 - Biometric_Data_Issue **(1 to M)**
 - Utente **(M to 1)** :um utente tem várias prescrições, e uma prescrição vários utentes
 - Médico **(M to 1)** :um médico tem várias prescrições, e uma prescrição tem apenas um médito
 - Duração **(Data)** :Data inicio e data fim
 - Notas **(String)**

#### Features

## Features

### Inscrição de Utentes
 - Número de Utente
 - Name
 - Data de nascimento
 - Data inscrição
 - Médico (que inscreveu)
 - Notas
 - Possíveis anexos (Ficha médica - Histórico médico) :a ponderar
### Consultas de estados de Utentes
 - Gráficos
 - Listas

### Consultas de prescrições

### Login para Pessoas
### Registos de dados (wearables, sensores, etc)
 - Tipo de prescrição (Ritmo cardiaco, Tensão arterial)
 - Fonte de dados (App Mobile, WS, WebSite)
 - Valor
 - Anexos (Possíveis anexos relevantes)
  Registo de exames (Exames médicos)
 - Tipo de exames Electrocardiogramas ECG, Radiografia do tórax)

### Deverá ainda permitir a gestão de itens de um PRC para determinado doente, com uma determinada duração, de acordo com os resultados obtidos desses processamentos de dados (por exemplo, sugerir, para o próximo mês, uma prescrição de exercício físico para doentes com obesidade, e/ou uma prescrição médica para doentes com índice de glicemia alto, e/ou uma prescrição de nutrição para doentes com níveis altos de colesterol).
 - Consoante o tipo de prescrição, ele irá adicionar adicionar essa prescrição aos Utentes
 - Se o utente teve o ULTIMO Biometric_Data value do Biometric_Data_Type "Peso" estiver contino no min e max do Biometric_Data_Issue a prescrição é dada a este utente.
 - Caso de estudo:
  1. Existe um *Biometric_Data_Type* para a **temperatura corporal** com **max** de **45** e o **min** de **30** **Cº (Graus)**
  2. É definido o *Biometric_Data_Issue* **Febre** com o **min** de **38** e o **max** de **45**
  3. Dado *Patient* tem um *Biometric_Data* do *Biometric_Data_Type* **temperatura corporal** e com um valor de **39.5** **Cº (Graus)**
  4. Um *Doctor* do *Patient* cria uma *Prescription* com *Biometric_Data_Issue* de **Febre** com o **start_date** de **25/11/2021** e **end_date** de **01/01/2022**


## Dúvidas
 - CascadeType.* :que tipo de cascade não permite a remoção enquanto houver entidades relacionadas
 - Quando estamos a guardar uma password, devemos encriptá-la no lado do cliente ou do lado do servidor? Se o fizermos do lado do servidor se houver um ManInTheMiddle podemos expôr a nossa password. Se o fizermos do lado do cliente como sabemos se é aquele utilizador que está pedindo as informações?

## Exemplos

Biometric_Data_Type
  name: "Temperatura corporal"
  min: 30
  max: 45
  measurement_unit: "ºC (Graus Celsius)"

Biometric_Data_Issue
  name: "Febre"
  min: 38
  max: 40
  Biometric_Data_Type: id:"Temperatura corporal"

  name: "Febre Critica"
  min: 40
  max: 45
  Biometric_Data_Type: id:"Temperatura corporal"

  name: "Hipotremia"
  min: 30
  max: 35
  Biometric_Data_Type: id:"Temperatura corporal"
