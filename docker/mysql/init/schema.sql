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
INSERT INTO `demande_conge` (`user_id`, `nom`, `prenom`, `date_debut`, `date_fin`, `type_conge`, `statut`, `motif`, `commentaires_manager`, `date_soumission`, `date_modification`, `solde_demande`, `duree_jours_ouvres`) VALUES
('1', 'Martin', 'Léa', '2025-10-15', '2025-10-25', 'CONGES_PAYES', 'EN_ATTENTE', 'Vacances en famille à la montagne', NULL, CURDATE(), NULL, 25, 7);

-- Demande de Chloé Lefebvre (approuvée)
INSERT INTO `demande_conge` (`user_id`, `nom`, `prenom`, `date_debut`, `date_fin`, `type_conge`, `statut`, `motif`, `commentaires_manager`, `date_soumission`, `date_modification`, `solde_demande`, `duree_jours_ouvres`) VALUES
('3', 'Lefebvre', 'Chloe', '2025-09-01', '2025-09-05', 'RTT', 'APPROUVEE', 'Pour un week-end prolongé', 'Demande approuvée. Profitez-en!', CURDATE() - INTERVAL 10 DAY, CURDATE() - INTERVAL 8 DAY, 18, 5);

-- Demande de Léa Martin (refusée)
INSERT INTO `demande_conge` (`user_id`, `nom`, `prenom`, `date_debut`, `date_fin`, `type_conge`, `statut`, `motif`, `commentaires_manager`, `date_soumission`, `date_modification`, `solde_demande`, `duree_jours_ouvres`) VALUES
('1', 'Martin', 'Léa', '2026-01-05', '2026-01-10', 'SANS_SOLDE', 'REFUSEE', 'Mariage d''un ami', 'Période de forte activité, impossible d''approuver.', CURDATE(), CURDATE(), 25, 4);


CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` varchar(50) NOT NULL,
  `nom` varchar(45) DEFAULT NULL,
  `prenom` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` varchar(25) DEFAULT NULL,
  `solde_demande` int DEFAULT '30',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `role`, `solde_demande`) VALUES
('1', 'Martin', 'Léa', 'lea.martin@societe.com', 'EMPLOYE', 25),
('2', 'Dubois', 'Marc', 'marc.dubois@societe.com', 'EMPLOYE', 30),
('3', 'Lefebvre', 'Chloe', 'chloe.lefebvre@societe.com', 'EMPLOYE', 18),
('4', 'Girard', 'Sophie', 'sophie.girard@societe.com', 'ADMIN', 40);

CREATE VIEW IF NOT EXISTS `vue_demande_conge_utilisateur` AS select `d`.`id` AS `id`,`d`.`user_id` AS `user_id`,`d`.`date_debut` AS `date_debut`,`d`.`date_fin` AS `date_fin`,`d`.`type_conge` AS `type_conge`,`d`.`statut` AS `statut`,`d`.`motif` AS `motif`,`d`.`commentaires_manager` AS `commentaires_manager`,`d`.`date_soumission` AS `date_soumission`,`d`.`date_modification` AS `date_modification`,`d`.`solde_demande` AS `solde_demande`,`d`.`duree_jours_ouvres` AS `duree_jours_ouvres`,`u`.`nom` AS `nom`,`u`.`prenom` AS `prenom` from (`demande_conge` `d` join `utilisateur` `u` on((`d`.`user_id` = `u`.`id`)));
