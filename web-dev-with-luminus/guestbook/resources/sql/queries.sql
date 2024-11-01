-- :name save-message! :! :n
-- :doc create new message using name nad message keys
INSERT INTO guestbook
(name, message)
VALUES (:name, :message)

-- :name get-messages :? :*
-- :doc select all avaialble messages
SELECT * from guestbook


