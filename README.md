# Android lietotne datoru komponentu izvēles atbalstam (Bakalaura darbs)

### Autors: Vladislavs Boičenko

Visi lietotnes galvenie elementi atrodas mapē "main".

Pilns prototipa projekts atrodas failā "PCBuilderApp.zip".

Komponentu datubāze atrodas mapē "main/assets/components.db"main/assets/components.db, tajā ir 104 datoru komponenti (pievienoti manuāli).

| Funkcija | Komponents |
|----------|------------|
| Galvenā izvēlne (personalizācijas iestatījumi un piekļuve funkcijām) | MainActivity |
| Valodu atbalsts | CountryManager |
| Konfigurācijas izveide | ConfigurationActivity, ConfigurationAdapter |
| Komponentu izvēle | SelectComponentActivity, SelectComponentAdapter | 
| Komponentu filtrēšana | FilterComponentsActivity, FilterComponentsAdapter |
| Komponentu detalizētas specifikācijas parādīšana | ComponentDetailsActivity |

Prototipa galvenās aktivitātes:
  -  MainActivity
  -  ConfigurationActivity
  -  SelectComponentActivity, SelectComponentAdapter
  -  SavedConfigurationActivity
  -  ComponentDetailsActivity

Galvenās biznesa loģikas klases:
  -  CompatibilityCheck
  -  CountryManager

Mijiedarbība ar datubāzi ir realizēta, izmantojot:
  -  DatabaseHelper
  -  DatabaseProvider

Komponentu montāžas instrukcija sastāv no:
  -  activity_assembly_instruction.xml
  -  strings.xml (ietver komponentu savienošanas posmus)
