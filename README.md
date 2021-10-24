# Clinics

##Entidades (Colunas & Features)
  Pessoas
    Colunas
      - username (UNIQUE PK)
      - email (UNIQUE) :as username and can be null
      - password
      - name
      - gender
      - Created_at
      - Updated_at
      - Deleted_at
    Features
      - Login

  Administradores (extend Pessoas)
    Colunas
    Features
      - Adicionar Tipos de Prescrições
      - Visualização de histórico de dados/sinais biométricos dos utentes (em anónimo talvez, não têm de saber quem são os utentes)

  Utentes (extend Pessoas)
    Colunas
      - Número de Utente (UNIQUE)
      - Médicos (N to M)
      - Prescrições (1 to M)
      - Dados_Biométricos (N to M)
      - Created_by :only by Administradores/Médicos
    Features
      - Inscrição Própria
      - Consultas de estados próprios
      - Visualização de histórico de dados/sinais biométricos (Próprio)

  Médicos (extend Pessoas)
    Colunas
      - especialidade
      - Utentes (N to M)
      - Created_by (FK_USER) :only by Administradores
      - Prescrições (1 to M)
    Features
      - Inscrição de Utentes
      - Consultas de estados de Utentes
      - Atribuir Prescrições aos Utentes
      - Gerir os dados/sinais biométricos dos seus utentes

  Tipos_De_Dados_Biométricos (Alimentação, Exercício regular, Temperatura corporal, Frequência cardíaca)
    Colunas
      - id (UNIQUE PK)
      - name :temperatura corporal (pode este campo ser a PK?)
      - limite_min :30(º)
      - limite_max :45(º)
      - unidade_medida (STRING) :ºC (Graus)
    Features
      - Notificar os Utentes e Médicos

  Dados_Biométricos (Resultados)
    Colunas
      - id (UNIQUE PK)
      - Tipos_De_Dados_Biométricos (1 to M)
      - Valores
      - Notas (String)

  Prescrições (uma prescrição de exercício físico para doentes com obesidade)
    Colunas
      - id (UNIQUE PK)
      - Tipos_De_Dados_Biométricos (1 to M)
      - Utente (M to 1) :um utente tem várias prescrições, e uma prescrição vários utentes
      - Médico (M to 1) :um médico tem várias prescrições, e uma prescrição tem apenas um médito
      - Duração (Data) :Data inicio e data fim
      - Notas (String)

##Features
  Inscrição de Utentes
    - Número de Utente
    - Nome
    - Data de nascimento
    - Data inscrição
    - Médico (que inscreveu)
    - Notas
    - Possíveis anexos (Ficha médica - Histórico médico) :a ponderar
  Consultas de estados de Utentes
    - Gráficos
    - Listas
  Consultas de prescrições
  Login para Pessoas
  Registos de dados (wearables, sensores, etc)
    - Tipo de prescrição (Ritmo cardiaco, Tensão arterial)
    - Fonte de dados (App Mobile, WS, WebSite)
    - Valor
    - Anexos (Possíveis anexos relevantes)
  Registo de exames (Exames médicos)
    - Tipo de exames Electrocardiogramas ECG, Radiografia do tórax)
  Deverá ainda permitir a gestão de itens de um PRC para determinado doente, com uma determinada duração, de acordo com os resultados obtidos desses processamentos de dados (por exemplo, sugerir, para o próximo mês, uma prescrição de exercício físico para doentes com obesidade, e/ou uma prescrição médica para doentes com índice de glicemia alto, e/ou uma prescrição de nutrição para doentes com níveis altos de colesterol).
    -  Consoante o tipo de prescrição, ele irá adicionar adicionar essa prescrição aos Utentes

##Dúvidas
  - CascadeType.* :que tipo de cascade não permite a remoção enquanto houver entidades relacionadas
