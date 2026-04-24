{- Grupo: X
   Integrante(s):
     Guani, Joaquin, 5.334.276-4
     Kasek, Lukas, 5.446.964-2
-}

module TypedJSON where

import AST
import JSONLibrary
import Control.Monad
import Data.List


-- Tipos JSON
data JSONType
  = TyString
  | TyNum
  | TyObject (Object JSONType)
  | TyArray JSONType
  | TyBool
  | TyNull
  deriving (Show, Eq)


arrayHomo :: [JSON] -> Bool
arrayHomo [] = False     -- si querés que [] sea invalido
arrayHomo (x:xs) = arrayHomoUltra (typeOf x) xs

arrayHomoUltra :: Maybe JSONType -> [JSON] -> Bool
arrayHomoUltra _ [] = True
arrayHomoUltra ty (x:xs) = ty == typeOf x && arrayHomoUltra ty xs


-- dado un valor JSON se infiere el tipo. Se devuelve
-- Nothing si el valor está mal tipado
typeOf :: JSON -> Maybe JSONType
typeOf (JString s) = Just TyString
typeOf (JNumber n) = Just TyNum
typeOf (JBoolean b) = Just TyBool
typeOf JNull = Just TyNull
-- tengo que usar el case, porque asi desarmo el maybe
typeOf (JArray xs) = if arrayHomo xs then case typeOf (head xs) of
           Just t  -> Just (TyArray t)
           Nothing -> Nothing
    else Nothing
typeOf (JObject (xs)) = let ys = sortKeys xs in 
                        case mapTypeOfSnd ys of
                          Just rs -> Just (TyObject rs)
                          Nothing -> Nothing


mapTypeOfSnd :: [(a, JSON)] -> Maybe[(a,JSONType)]
mapTypeOfSnd [] = Just []
mapTypeOfSnd ((x,y):xs) = case (typeOf y) of
                      Nothing -> Nothing
                      Just t -> case mapTypeOfSnd xs of 
                        Nothing -> Nothing
                        Just ts -> Just ((x,t) : ts)

-- decide si las claves de un objeto están ordenadas
-- lexicográficamente y no se repiten.
objectWf :: Object JSONType -> Bool
objectWf [] = False
objectWf [_] = True 
objectWf ((k1,v1):(k2,v2):xs) = k1 < k2 && objectWf ((k2,v2):xs)

-- decide si todos los tipos objeto contenidos en un tipo JSON
-- están bien formados.
typeWf :: JSONType -> Bool
typeWf TyNum    = True
typeWf TyString = True
typeWf TyBool   = True
typeWf TyNull   = True
typeWf (TyArray t) = typeWf t
typeWf (TyObject obj)
    | not (objectWf obj) = False
    | otherwise          = chequeoTipo obj
  where
    chequeoTipo []            = True
    chequeoTipo ((_,t):xs)    = typeWf t && chequeoTipo xs

-- dado un valor JSON v, y un tipo t, decide si v tiene tipo t.
hasType :: JSON -> JSONType -> Bool
hasType v t = case typeOf v of
    Nothing -> False
    Just vv -> vv == t

--Solo para pruebas a mano
hasType2 :: JSON -> Maybe JSONType -> Bool
hasType2 v t = 
  case t of 
    Nothing -> False
    Just tt -> case typeOf v of
      Nothing -> False
      Just vv -> vv == tt