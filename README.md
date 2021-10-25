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
 - password
 - name
 - gender
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

### Biometric_Data_Type (Alimentação, Exercício regular, Temperatura corporal, Frequência cardíaca)

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
 - biometric_Data_Type **(1 to M)**
 - values **(Integer)**
 - notes **(String)**

### Prescriptions (uma prescrição de exercício físico para doentes com obesidade)

#### Columns

 - id (UNIQUE PK)
 - Tipos_De_Dados_Biométricos **(1 to M)**
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
 -  Consoante o tipo de prescrição, ele irá adicionar adicionar essa prescrição aos Utentes

## Dúvidas
 - CascadeType.* :que tipo de cascade não permite a remoção enquanto houver entidades relacionadas
