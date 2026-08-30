# Android lietotne datoru komponentu izvēles atbalstam (Bakalaura darbs)

### Autors: Vladislavs Boičenko

Visi lietotnes galvenie elementi atrodas mapē "main".

Pilns prototipa projekts atrodas failā "PCBuilderApp.zip".

Komponentu datubāze atrodas mapē "main/assets/components.db"main/assets/components.db, tajā ir 104 datoru komponenti (pievienoti manuāli).

### Galvenās aktivitātes un biznesa loģikas klases
| Funckija | Komponents |
|----------|------------|
| Galvenā izvēlne (personalizācijas iestatījumi un piekļuve funkcijām) | MainActivity |
| Konfigurācijas izveide | ConfigurationActivity |
| Komponentu izvēle | SelectComponentActivity | 
| Komponentu filtrēšana | FilterComponentsActivity |
| Komponentu detalizētas specifikācijas parādīšana | ComponentDetailsActivity |
| Konfigurāciju saglabāšana | SavedConfigurationActivity |
| Valstu atbalsts (valsts, valūta un valoda) | CountryManager |
| Savietojamības pārbaude | CheckCompatibility |
| Konfigurāciju analīze(CPU un GPU līdzsvara analīze un jaunināšanas ieteikumi) | AnalyzeConfigurationActivity, AnalyzeConfiguration |
| Montāžas instrukcija | InstructionActivity, InstructioData |

Mijiedarbība ar datubāzi ir realizēta, izmantojot:
  -  DatabaseHelper
  -  DatabaseProvider
 
Valodas dati tiek glabāti:
  -  strings.xml (lv)
  -  strings.xml (en)
