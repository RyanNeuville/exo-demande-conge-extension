CREATE TABLE IF NOT EXISTS `demande_conge` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `type_conge` varchar(50) NOT NULL,
  `statut` varchar(50) NOT NULL,
  `motif` varchar(500) DEFAULT NULL,
  `commentaires_manager` varchar(500) DEFAULT NULL,
  `date_soumission` date NOT NULL,
  `date_modification` date DEFAULT NULL,
  `solde_demande` int NOT NULL,
  `duree_jours_ouvres` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` varchar(50) NOT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `prenom` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` varchar(25) DEFAULT NULL,
  `solde_demande` int DEFAULT '30',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE VIEW IF NOT EXISTS `vue_demande_conge_utilisateur` AS select `d`.`id` AS `id`,`d`.`user_id` AS `user_id`,`d`.`date_debut` AS `date_debut`,`d`.`date_fin` AS `date_fin`,`d`.`type_conge` AS `type_conge`,`d`.`statut` AS `statut`,`d`.`motif` AS `motif`,`d`.`commentaires_manager` AS `commentaires_manager`,`d`.`date_soumission` AS `date_soumission`,`d`.`date_modification` AS `date_modification`,`d`.`solde_demande` AS `solde_demande`,`d`.`duree_jours_ouvres` AS `duree_jours_ouvres`,`u`.`nom` AS `nom`,`u`.`prenom` AS `prenom` from (`demande_conge` `d` join `utilisateur` `u` on((`d`.`user_id` = `u`.`id`)));
